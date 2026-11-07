package com.example.demo.security;
import com.example.demo.domain.usuario.Usuario; import io.jsonwebtoken.Jwts; import io.jsonwebtoken.security.Keys; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service; import javax.crypto.SecretKey; import java.nio.charset.StandardCharsets; import java.time.Instant; import java.util.Date;
@Service public class JwtService {
 private final SecretKey key; private final long duration;
 public JwtService(@Value("${app.jwt.secret}") String secret,@Value("${app.jwt.duration-ms:3600000}") long duration){if(secret.getBytes(StandardCharsets.UTF_8).length<32) throw new IllegalArgumentException("app.jwt.secret debe tener al menos 32 bytes"); this.key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));this.duration=duration;}
 public String generar(Usuario u){Instant now=Instant.now();return Jwts.builder().subject(u.id().toString()).claim("rol",u.rol().name()).issuedAt(Date.from(now)).expiration(Date.from(now.plusMillis(duration))).signWith(key).compact();}
 public Claims validar(String token){var c=Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();return new Claims(Long.valueOf(c.getSubject()),Usuario.Rol.valueOf(c.get("rol",String.class)));}
 public record Claims(Long usuarioId,Usuario.Rol rol){}
}
