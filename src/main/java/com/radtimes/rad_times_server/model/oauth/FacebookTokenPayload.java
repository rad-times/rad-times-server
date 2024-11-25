package com.radtimes.rad_times_server.model.oauth;

import lombok.Data;

@Data
public class FacebookTokenPayload {
    public String iss;
    public String aud;
    public String sub;
    public Long exp;
    public String nonce;
    public String email;
    public String picture;;
    public String given_name;
    public String family_name;
}
