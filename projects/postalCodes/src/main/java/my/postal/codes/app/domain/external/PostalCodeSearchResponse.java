package my.postal.codes.app.domain.external;

import com.google.common.base.MoreObjects;

public class PostalCodeSearchResponse {
    private String status;
    private PostalCodeSearchInformation result;

    public class PostalCodeSearchInformation{
        private String postcode;
        private String longitude;
        private String latitude;
        private String lsoa;
        private String admin_ward;
        private String country;

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLsoa() {
            return lsoa;
        }

        public void setLsoa(String lsoa) {
            this.lsoa = lsoa;
        }

        public String getAdmin_ward() {
            return admin_ward;
        }

        public void setAdmin_ward(String admin_ward) {
            this.admin_ward = admin_ward;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("postCode", postcode)
                    .add("longitude", longitude)
                    .add("latitude", latitude)
                    .add("lsoa", lsoa)
                    .add("admin_ward", admin_ward)
                    .add("country", country)
                    .toString();
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PostalCodeSearchInformation getResult() {
        return result;
    }

    public void setResult(PostalCodeSearchInformation result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("status", status)
                .add("result", result)
                .toString();
    }
}


