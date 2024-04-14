package sixgaezzang.sidepeek.auth.oauth.service.strategy;

import sixgaezzang.sidepeek.auth.oauth.OAuth2User;

public interface OAuth2Manager {

    public OAuth2User getOauth2User(String code);
}
