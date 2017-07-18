package com.realsight.westworld.video.test;
/*
import org.datavec.api.util.ClassPathResource;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.trainedmodels.TrainedModels;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import java.io.File;

import java.util.*;
/**
 * Created by tomhanlon on 4/20/17.
 */
/*public class KerasModelimportInceptionV3 {
    public INDArray getFeature(String layerName, String fileName) throws Exception {
        int imgWidth = 299;
        int imgHeight = 299;
        int imgChannels = 3;
        int numClasses = 1000;
        ComputationGraph model = KerasModelImport.importKerasModelAndWeights("tmp/inception_v3_complete.h5");
        File elephant = new ClassPathResource(fileName).getFile();
        NativeImageLoader imageLoader = new NativeImageLoader(imgHeight, imgWidth, imgChannels);
        INDArray image = imageLoader.asMatrix(elephant).div(255.0).sub(0.5).mul(2);
        Map<String, INDArray> map = model.feedForward(image, false);
        return map.get(layerName);
    }

}*/