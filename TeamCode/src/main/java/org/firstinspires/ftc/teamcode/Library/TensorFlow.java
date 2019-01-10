package org.firstinspires.ftc.teamcode.Library;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class TensorFlow {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY = "AVRfS7L/////AAABmaGT8EE2D0ognaN6WhR7wTsd4Zu3Bn3gJjl8WAi95O+bXTS8qogcR58wbZP+UoYB99sjHS22e4oF03SQ5f3y0j9oUyDrOw6vbqPCmductE5WDpTqj+RQIbkUX/0zAmOIsLdq0a7jWPEPAGtI5RRVD3+pFqwU8jvy16q0zvTa+zpvcQU4uYDTOtLEwhGUnStDbK8sgrNzjehUojKnMezx5ypO0C69YT+N8nChher2V+kghuea9ysf4auTD2vIhL7mw8oEZKDcJd3kf9hLX8dlukarDrVcyT+pDC92zARDSWybAU7IxHvol627lXekBv+lo+Jv9UNUvma6tSB4AR7zeBmnhMEMmXoOau7JABDkzT9m";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private HardwareMap hwMap;

    private int LEFT_THRESHOLD = 275;
    private int RIGHT_THRESHOLD = 475;

    private int goldMineralX;

    public enum GoldPosition {
        LEFT,
        CENTER,
        RIGHT
    }

    public void init(HardwareMap hardwareMap) {
        hwMap = hardwareMap;
        initVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            throw new RuntimeException("This phone is not compatible with Tensorflow");
        }
    }

    public void activate() {
        /* Activate Tensor Flow Object Detection. */
        if (tfod != null) {
            tfod.activate();
        }
    }

    public GoldPosition getGoldPos() {
        GoldPosition goldPosition;
        determineGoldMineralX();
        if (goldMineralX < LEFT_THRESHOLD) {
            goldPosition = GoldPosition.LEFT;
        } else if (goldMineralX > RIGHT_THRESHOLD) {
            goldPosition = GoldPosition.RIGHT;
        } else {
            goldPosition = GoldPosition.CENTER;
        }
        return goldPosition;
    }

    private void determineGoldMineralX() {
        if (tfod != null) {
            Recognition goldMineral = identifyGoldMineral();
            if (goldMineral != null) {
                goldMineralX = (int) goldMineral.getLeft();
            }
        }
    }

    private Recognition identifyGoldMineral() {
        Recognition goldMineral = null;
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            for (Recognition recognition : updatedRecognitions) {
                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                    goldMineral = recognition;
                }
            }
        }
        return goldMineral;
    }

    public void shutdown() {
        if (tfod != null) {
            tfod.shutdown();
        }
    }

    public int getGoldMineralX() {
        return goldMineralX;
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hwMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hwMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hwMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}
