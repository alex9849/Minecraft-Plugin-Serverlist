package net.alex9849.web.endpoints;

import net.alex9849.web.exception.ForbiddenException;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1")
public class Endpoint {

    @RequestMapping(value = "sendstats", method = RequestMethod.POST)
    public UUID sendStats(HttpServletRequest request) throws ForbiddenException {
        if(!Objects.equals(request.getHeader("User-Agent"), "Analytic Plugin")) {
            throw new ForbiddenException();
        }
        return UUID.randomUUID();
    }

}
