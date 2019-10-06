package hw18.micronault.controller;

import hw18.micronault.entity.Info;
import hw18.micronault.service.ServerInfoService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;

import javax.inject.Inject;
import java.security.Principal;

@Secured("isAuthenticated()")
@Controller("/")
public class InfoController {

    private final ServerInfoService serverInfoService;

    @Inject
    public InfoController(ServerInfoService serverInfoService) {
        this.serverInfoService = serverInfoService;
    }

    @Get("/information")
    public Info index(Principal principal) {
        Info info = serverInfoService.getInfoFromServer();
        info.setTextInfo(info.getTextInfo() + " for " + principal.getName());
        return info;
    }
}