package net.alex9849.pluginstats.web.model.response;

public class InstallationResponse {

    private boolean enablePremiumFeatures;
    private String installId;

    public boolean isEnablePremiumFeatures() {
        return enablePremiumFeatures;
    }

    public void setEnablePremiumFeatures(boolean enablePremiumFeatures) {
        this.enablePremiumFeatures = enablePremiumFeatures;
    }

    public String getInstallId() {
        return installId;
    }

    public void setInstallId(String installId) {
        this.installId = installId;
    }
}
