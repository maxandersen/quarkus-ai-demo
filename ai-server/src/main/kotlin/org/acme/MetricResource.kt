package org.acme

import io.opentelemetry.api.metrics.LongCounter
import io.opentelemetry.api.metrics.Meter
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.jboss.logging.Logger

private val LOG: Logger = Logger.getLogger(MetricResource::class.java)

@Path("/hello-metrics")
open class MetricResource(
    meter: Meter,
) {
    private val counter: LongCounter =
        meter
            .counterBuilder("hello-metrics")
            .setDescription("hello-metrics")
            .setUnit("invocations")
            .build()

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(): String {
        counter.add(1)
        LOG.info("hello-metrics")
        return "hello-metrics"
    }
}
