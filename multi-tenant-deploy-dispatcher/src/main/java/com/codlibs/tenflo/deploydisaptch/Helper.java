package com.codlibs.tenflo.deploydisaptch;

public class Helper {
    public static String mkRole(String tenantId) {
        return "arn:aws:iam::000000000000:role/tenflo-%s-payments-role".formatted(tenantId);
    }
}
