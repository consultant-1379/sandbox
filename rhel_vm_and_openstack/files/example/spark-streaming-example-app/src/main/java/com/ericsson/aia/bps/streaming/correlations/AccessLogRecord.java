package com.ericsson.aia.bps.streaming.correlations;

import java.util.HashMap;
import java.util.Map;

import nl.basjes.parse.core.Field;

public class AccessLogRecord {
    private final Map<String, String> results = new HashMap<>(32);

    @SuppressWarnings("UnusedDeclaration")
    @Field({
        "STRING:request.firstline.uri.query.*",            
        "IP:connection.client.host",
        "NUMBER:connection.client.logname",
        "STRING:connection.client.user",
        "TIME.STAMP:request.receive.time",            
        "HTTP.URI:request.firstline.uri",
        "STRING:request.status.last",
        "BYTES:response.body.bytesclf",
        "HTTP.URI:request.referer",
        "STRING:request.referer.query.mies",
        "STRING:request.referer.query.wim",
        "HTTP.USERAGENT:request.user-agent",
        "TIME.DAY:request.receive.time.day",
        "TIME.HOUR:request.receive.time.hour",
        "TIME.MONTHNAME:request.receive.time.monthname",
        "TIME.EPOCH:request.receive.time.epoch",
        "TIME.WEEK:request.receive.time.weekofweekyear",
        "TIME.YEAR:request.receive.time.weekyear",
        "TIME.YEAR:request.receive.time.year",
        "TIME.SECOND:request.receive.time.second"        
        })
    public void setValue(final String name, final String value) {
        results.put(name, value);
    }

    public Map<String, String> getResults() {
        return results;
    }
}

