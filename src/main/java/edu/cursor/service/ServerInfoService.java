package edu.cursor.service;

import edu.cursor.entity.Info;

import javax.inject.Singleton;

@Singleton
public class ServerInfoService {

    public Info getInfoFromServer() {
        return new Info(777L,"this is secret info", 2.4214);
    }

}