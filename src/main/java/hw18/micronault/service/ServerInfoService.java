package hw18.micronault.service;


import hw18.micronault.entity.Info;

import javax.inject.Singleton;

@Singleton
public class ServerInfoService {

    public Info getInfoFromServer() {
        return new Info(777L, "this is secret info", 2.4214);
    }

}