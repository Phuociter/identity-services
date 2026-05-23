package com.example.indentity.service;

import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import com.example.indentity.dto.request.AuthenticationRequest;
import com.example.indentity.exception.AppException;
import com.example.indentity.exception.ErrorCode;
import com.example.indentity.repository.UserRepository;
import com.example.indentity.dto.response.AuthenticationResponse;
import com.example.indentity.dto.request.IntrospectRequest;
import com.example.indentity.dto.response.IntrospectResponse;
import com.example.indentity.entity.User;

import lombok.RequiredArgsConstructor;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;// dùng để mã hóa mật khẩu bằng thuật toán bcrypt
import org.springframework.security.crypto.password.PasswordEncoder;// dùng để mã hóa mật khẩu và xác thực mật khẩu đã mã hóa
import org.springframework.stereotype.Service;// dùng cho việc
import com.nimbusds.jose.JWSObject;// tao token JWT
import com.nimbusds.jose.JWSVerifier;// xac thuc token
import com.nimbusds.jwt.JWTClaimsSet;// tao payload cho token
import com.nimbusds.jose.JWSHeader;// tao header cho token
import com.nimbusds.jose.JWSAlgorithm;// xac dinh thuat toan ma hoa cho token
import com.nimbusds.jose.JOSEException;// xac dinh loi khi tao token(lỗi giải mẫ)
import java.text.ParseException;// xác định lỗi khi phân tích cú pháp token JWT (ví dụ: token không hợp lệ hoặc không thể phân tích được)
import com.nimbusds.jose.Payload;// tao payload cho token
import com.nimbusds.jose.crypto.MACSigner;// tao sign
import com.nimbusds.jose.crypto.MACVerifier;// xac thuc token
import com.nimbusds.jwt.SignedJWT;// tao token JWT co ky
import java.time.Instant;//dùng để làm việc với thời gian, đặc biệt là khi cần tính toán thời gian hết hạn của token JWT
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;// tao log

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthenticationService {
    
    UserRepository userRepository;
    @NonFinal//anotation cua lombok de cho phep thay doi gia tri cua bien khi đang dùng BUILD
    @Value("${jwt.secret}")//annotation cho phep lấy giá trị từ yml
    protected String SIGNER_KEY;

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername()).
        orElseThrow(()-> new AppException(ErrorCode.USER_NOTEXISTS));
        
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        
        var token = generateToken(user);

        return AuthenticationResponse.builder()
            .authenticated(true)
            .token(token)
            .build();
    }

// cau truc mot token JWT bao gom header, payload va signature
    private String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
            .subject(user.getUsername())
            .issuer("intern.com")
            .issueTime(new Date())
            .expirationTime(new Date(
                Instant.now().plus(1,ChronoUnit.HOURS).toEpochMilli())// token sẽ hết hạn sau 1 giờ(tính mốc thời gian sau 1 giờ)
            )
            .claim("scope", buildScope(user))// thêm một claim tùy chỉnh có tên là "scope" với giá trị "user" vào payload của token JWT
            .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());// payload là dữ liệu của token được mã hóa dưới dạng JSON

        JWSObject jwsObject = new JWSObject(header, payload);//dùng để tạo đối tượng JWS
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY));
            return jwsObject.serialize();// serialize() sẽ trả về token dưới dạng chuỗi
        } catch (JOSEException e) {
            log.error("can not create token", e);
            throw new RuntimeException("Error signing the token", e);
        }

    }

    public IntrospectResponse introspect(IntrospectRequest request) 
    throws JOSEException,ParseException{

        var token = request.getToken();
        
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expitytime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
        .valid(verified && expitytime.after(new Date()))
        .build();
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(stringJoiner::add);

        return stringJoiner.toString();
    }
}
