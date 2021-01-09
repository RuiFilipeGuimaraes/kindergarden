package my.postal.codes.app.enricher.api;

public interface DataEnricher<A, B> {

    B enrich(A objectToEnrich, String userId);
}
