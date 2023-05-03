package net.interfax.rest.client.impl;

import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

@FunctionalInterface
public interface JerseyRequestExecutor {

    Response readyTheTargetAndExecute(WebTarget target);
}
