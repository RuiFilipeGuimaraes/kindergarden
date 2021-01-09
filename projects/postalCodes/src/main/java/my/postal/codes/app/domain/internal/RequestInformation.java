package my.postal.codes.app.domain.internal;

import com.google.common.base.MoreObjects;

public class RequestInformation {
    final String postalCode;
    final String userId;

    public RequestInformation(String postalCode, String userId) {
        this.postalCode = postalCode;
        this.userId = userId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("postalCode", postalCode)
                .add("userId", userId)
                .toString();
    }
}
