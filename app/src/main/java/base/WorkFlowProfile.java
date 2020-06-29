package base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorkFlowProfile {
    @SerializedName("Form Header")
    @Expose
    private FormHeader formHeader;
    @SerializedName("Form Body")
    @Expose
    private FormBody formBody;

    public class FormBody {

        @SerializedName("Label")
        @Expose
        private String label;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("value")
        @Expose
        private String value;
    }


    public class FormHeader {

        @SerializedName("Form Header")
        @Expose
        private List<FormHeader> formHeader = null;
        @SerializedName("label")
        @Expose
        private String label;
        @SerializedName("Form Body")
        @Expose
        private List<FormBody> formBody = null;
    }
}
