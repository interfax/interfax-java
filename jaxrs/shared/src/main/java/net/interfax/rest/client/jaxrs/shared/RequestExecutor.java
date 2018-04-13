package net.interfax.rest.client.jaxrs.shared;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@FunctionalInterface
public interface RequestExecutor {

    Response readyTheTargetAndExecute(WebTarget target);
}
