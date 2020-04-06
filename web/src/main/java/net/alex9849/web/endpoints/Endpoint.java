package net.alex9849.web.endpoints;

import net.alex9849.web.exception.ForbiddenException;
import net.alex9849.web.model.PluginInstallationDTO;
import net.alex9849.web.service.PluginInstallationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api/v1")
public class Endpoint {

    @Autowired
    PluginInstallationService pls;

    @RequestMapping(value = "sendstats", method = RequestMethod.PUT)
    public String sendStats(HttpServletRequest request, @RequestBody() PluginInstallationDTO piDto,
                          @RequestParam(required = false, defaultValue = "false") boolean startup) throws ForbiddenException {
        /*if(!Objects.equals(request.getHeader("User-Agent"), "Analytic Plugin")) {
            throw new ForbiddenException();
        }*/
        return pls.processStats(piDto, startup).getUuid();
    }

}
