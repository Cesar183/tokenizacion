package co.com.app.jpa.config;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class DBSecret {
    private String url;
    private String username;
    private String password;
    private String dbClusterIdentifier;
    private String dbname;
    private String engine;
    private int port;
    private String host;
}
