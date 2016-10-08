package net.interfax.rest.client.impl;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@FunctionalInterface
public interface JerseyRequestExecutor {

    Response readyTheTargetAndExecute(WebTarget target);
}
