package edu.cursor.controller;

import edu.cursor.entity.Info;
import edu.cursor.service.ServerInfoService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;

import javax.inject.Inject;
import java.security.Principal;

@Secured("isAuthenticated()")
@Controller
public class InfoController {

    @Inject
    private ServerInfoService serverInfoService;

    @Get("/info")
    public Info index(Principal principal) {
        Info info = serverInfoService.getInfoFromServer();
        info.setTextInfo(info.getTextInfo() + " for " + principal.getName());
        return info;
    }
}