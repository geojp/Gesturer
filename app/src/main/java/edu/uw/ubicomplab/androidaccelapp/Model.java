package edu.uw.ubicomplab.androidaccelapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class Model {
    // Examples of how the .arff format:
    // https://www.programcreek.com/2013/01/a-simple-machine-learning-example-in-java/
    // https://www.cs.waikato.ac.nz/~ml/weka/arff.html
    private Map<String, ArrayList<String[]>> trainingData;
    private String[] testData;
    public Map<String, String> featureNames;
    private String trainDataFilepath = "trainData.arff";
    private String testDataFilepath = "testData.arff";
    private Classifier model;
    private Context context;

    // TODO optional: give your gestures more informative names
    public String[] outputClasses = {"RollLeft", "Circle", "SlideUpDown"};

    public Model(Context context) {
        this.context = context;
        resetTrainingData();

        // Specify the features
        featureNames = new TreeMap<>();
        // TODO optional: create more features with more informative names
        featureNames.put("Ax_Mean", "numeric");
        featureNames.put("Ax_Min", "numeric");
        featureNames.put("Ax_Max", "numeric");
        featureNames.put("Ax_Variance", "numeric");
        featureNames.put("Ax_Kurtosis", "numeric");
        featureNames.put("Ax_EnergyMean", "numeric");
        featureNames.put("Ax_Skewness", "numeric");

        featureNames.put("Ay_Mean", "numeric");
        featureNames.put("Ay_Min", "numeric");
        featureNames.put("Ay_Max", "numeric");
        featureNames.put("Ay_Variance","numeric");
        featureNames.put("Ay_Kurtosis", "numeric");
        featureNames.put("Ay_EnergyMean", "numeric");
        featureNames.put("Ay_Skewness", "numeric");

        featureNames.put("Az_Mean", "numeric");
        featureNames.put("Az_Min", "numeric");
        featureNames.put("Az_Max", "numeric");
        featureNames.put("Az_Variance","numeric");
        featureNames.put("Az_Kurtosis", "numeric");
        featureNames.put("Az_EnergyMean", "numeric");
        featureNames.put("Az_Skewness", "numeric");

        featureNames.put("Gx_Mean", "numeric");
        featureNames.put("Gx_Min", "numeric");
        featureNames.put("Gx_Max", "numeric");
        featureNames.put("Gx_Variance","numeric");
        featureNames.put("Gx_Kurtosis", "numeric");
        featureNames.put("Gx_EnergyMean", "numeric");
        featureNames.put("Gx_Skewness", "numeric");

        featureNames.put("Gy_Mean", "numeric");
        featureNames.put("Gy_Min", "numeric");
        featureNames.put("Gy_Max", "numeric");
        featureNames.put("Gy_Variance","numeric");
        featureNames.put("Gy_Kurtosis", "numeric");
        featureNames.put("Gy_EnergyMean", "numeric");
        featureNames.put("Gy_Skewness", "numeric");

        featureNames.put("Gz_Mean", "numeric");
        featureNames.put("Gz_Min", "numeric");
        featureNames.put("Gz_Max", "numeric");
        featureNames.put("Gz_Variance","numeric");
        featureNames.put("Gz_Kurtosis", "numeric");
        featureNames.put("Gz_EnergyMean", "numeric");
        featureNames.put("Gz_Skewness", "numeric");
    }

    public double GetEnergyMean(DescriptiveStatistics data) {

        double temp[] = data.getValues();

        // get abs value
        for (int i = 0; i < temp.length; i++)
        {
            temp[i] = Math.abs(temp[i]);
        }

        DescriptiveStatistics val = new DescriptiveStatistics(temp);
        return val.getMean();
    }

    /**
     * Add a sample to the training or testing set with the corresponding label
     * @param atime: the time for the accelerometer data
     * @param ax: the x-acceleration data
     * @param ay: the y-acceleration data
     * @param az: the z-acceleration data
     * @param gtime: the time for the accelerometer data
     * @param gx: the x-gyroscope data
     * @param gy: the y-gyroscope data
     * @param gz: the z-gyroscope data
     * @param outputLabel: the label for the data
     * @param isTraining: whether the sample should go into the train or test set
     */
    public void addSample(DescriptiveStatistics atime, DescriptiveStatistics ax, DescriptiveStatistics ay, DescriptiveStatistics az,
                             DescriptiveStatistics gtime, DescriptiveStatistics gx, DescriptiveStatistics gy, DescriptiveStatistics gz,
                             String outputLabel, boolean isTraining) {
        Double[] data = new Double[featureNames.keySet().size()];
        int wIndex = 0;

        // Compute features
        // TODO: replace these placeholders with real calculations involving ax, ay, etc.
        data[wIndex++] = ax.getGeometricMean();
        data[wIndex++] = ax.getMax();
        data[wIndex++] = ax.getMin();
        data[wIndex++] = ax.getVariance();
        data[wIndex++] = ax.getKurtosis();
        data[wIndex++] = GetEnergyMean(ax);
        data[wIndex++] = ax.getSkewness();

        data[wIndex++] = ay.getGeometricMean();
        data[wIndex++] = ay.getMax();
        data[wIndex++] = ay.getMin();
        data[wIndex++] = ay.getVariance();
        data[wIndex++] = ay.getKurtosis();
        data[wIndex++] = GetEnergyMean(ay);
        data[wIndex++] = ay.getSkewness();

        data[wIndex++] = az.getGeometricMean();
        data[wIndex++] = az.getMax();
        data[wIndex++] = az.getMin();
        data[wIndex++] = az.getVariance();
        data[wIndex++] = az.getKurtosis();
        data[wIndex++] = GetEnergyMean(az);
        data[wIndex++] = az.getSkewness();

        data[wIndex++] = gx.getGeometricMean();
        data[wIndex++] = gx.getMax();
        data[wIndex++] = gx.getMin();
        data[wIndex++] = gx.getVariance();
        data[wIndex++] = gx.getKurtosis();
        data[wIndex++] = GetEnergyMean(gx);
        data[wIndex++] = gx.getSkewness();

        data[wIndex++] = gy.getGeometricMean();
        data[wIndex++] = gy.getMax();
        data[wIndex++] = gy.getMin();
        data[wIndex++] = gy.getVariance();
        data[wIndex++] = gy.getKurtosis();
        data[wIndex++] = GetEnergyMean(gy);
        data[wIndex++] = gy.getSkewness();

        data[wIndex++] = gz.getGeometricMean();
        data[wIndex++] = gz.getMax();
        data[wIndex++] = gz.getMin();
        data[wIndex++] = gz.getVariance();
        data[wIndex++] = gz.getKurtosis();
        data[wIndex++] = GetEnergyMean(gz);
        data[wIndex++] = gz.getSkewness();

        // Convert the feature vector to Strings
        String[] stringData = new String[featureNames.keySet().size()];
        for (int i=0; i<featureNames.keySet().size(); i++) {
            stringData[i] = Double.toString(data[i]);
        }

        // Add to the list of feature samples as strings
        if (isTraining) {
            ArrayList<String[]> currentSamples = trainingData.get(outputLabel);
            currentSamples.add(stringData);
            trainingData.put(outputLabel, currentSamples);
        }
        else {
            testData = stringData;
        }
    }

    /**
     * Clears all of the data for the model
     */
    public void resetTrainingData() {
        // Create a blank list for each gesture
        trainingData = new LinkedHashMap<>();
        for (String s: outputClasses) {
            trainingData.put(s, new ArrayList<String[]>());
        }
    }

    /**
     * Returns the number of training samples for the given class index
     * @param index: the class index
     * @return the number of samples for the given class index
     */
    public int getNumTrainSamples(int index) {
        String className = outputClasses[index];
        return trainingData.get(className).size();
    }

    /**
     * Create an .arff file for the dataset
     * @param isTraining: whether the data is training or testing data
     */
    private void createDataFile(boolean isTraining) {
        PrintWriter writer;
        // Setup the file writer depending on whether it is training or testing data
        if (isTraining)
            writer = createPrintWriter(trainDataFilepath);
        else
            writer = createPrintWriter(testDataFilepath);

        // Name the dataset
        writer.println("@relation gestures");
        writer.println("");

        // Define the features
        for (String s: featureNames.keySet()) {
            writer.println("@attribute "+s+" "+featureNames.get(s));
        }

        // Define the possible output classes
        String outputOptions = "@attribute gestureName {";
        for (String s: outputClasses) {
            outputOptions += s+", ";
        }
        outputOptions = outputOptions.substring(0, outputOptions.length()-2);
        outputOptions += "}";
        writer.println(outputOptions);
        writer.println("");

        // Write the data
        writer.println("@data");
        if (isTraining) {
            // Go through each category of possible outputs and save their samples
            for (String s: outputClasses) {
                ArrayList<String[]> gestureSamples = trainingData.get(s);
                for (String[] sampleData: gestureSamples) {
                    String sample = "";
                    for (String x: sampleData) {
                        sample += x+",";
                    }
                    sample += s;
                    writer.println(sample);
                }
            }
        }
        else {
            // Write the new sample with a blank label
            String sample = "";
            for (String x: testData) {
                sample += x+",";
            }
            sample += "?";
            writer.println(sample);
        }
        writer.close();
    }

    /**
     * Trains a model for the training data
     */
    public void train() {
        // Create the file for training
        createDataFile(true);

        // Read the file and specify the last index as the class
        Instances trainInstances = createInstances(trainDataFilepath);
        if (trainInstances == null) {
            return;
        }
        trainInstances.setClassIndex(trainInstances.numAttributes()-1);

        // Define the classifier
        // TODO optional: try out different classifiers provided by Weka
        model = new J48();
        try {
            model.buildClassifier(trainInstances);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * Returns the string label for the recently tested gesture
     * @return the string label
     */
    public String test() {
        // Create the file for testing
        createDataFile(false);

        // Read the file and specify the last index as the class
        Instances testInstances = createInstances(testDataFilepath);
        testInstances.setClassIndex(testInstances.numAttributes()-1);

        // Predict
        String classLabel = null;
        try {
            double classIndex = model.classifyInstance(testInstances.instance(0));
            classLabel = testInstances.classAttribute().value((int) classIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classLabel;
    }

    /**
     * Reads the .arff file and converts it into an Instances object
     * @param filename the filepath for the .arff file
     * @return a newly created Instances object
     */
    private Instances createInstances(String filename) {
        // Read the file
        File SDFile = android.os.Environment.getExternalStorageDirectory();
        String fullFileName = SDFile.getAbsolutePath() + File.separator + filename;
        BufferedReader dataReader;
        try {
            FileReader fileReader = new FileReader(fullFileName);
            dataReader = new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        // Create the training instance
        Instances instances;
        try {
            instances = new Instances(dataReader);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,
                    "Something is wrong with your .arff file!",
                    Toast.LENGTH_SHORT).show();
            return null;
        }
        return instances;
    }

    /**
     * Creates the file at the location
     * @param filename: the filename that appears at the root of external storage
     * @return writer: the PrintWriter object to be used
     */
    public PrintWriter createPrintWriter(String filename) {
        // Create the file
        File SDFile = android.os.Environment.getExternalStorageDirectory();
        String fullFileName = SDFile.getAbsolutePath() + File.separator + filename;
        PrintWriter writer;
        try {
            writer = new PrintWriter(fullFileName);
        } catch(FileNotFoundException e) {
            return null;
        }
        return writer;
    }
}
