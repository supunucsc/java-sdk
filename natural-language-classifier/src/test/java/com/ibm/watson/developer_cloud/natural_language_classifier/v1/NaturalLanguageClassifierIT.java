/**
 * Copyright 2017 IBM Corp. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.ibm.watson.developer_cloud.natural_language_classifier.v1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import com.ibm.watson.developer_cloud.WatsonServiceTest;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classifier.Status;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifierList;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifyOptions;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.CreateClassifierOptions;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.DeleteClassifierOptions;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.GetClassifierOptions;
import com.ibm.watson.developer_cloud.service.exception.NotFoundException;

import org.junit.Assume;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * The Class NaturalLanguageClassifierTest.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NaturalLanguageClassifierIT extends WatsonServiceTest {

  private static final String RESOURCE = "src/test/resources/natural_language_classifier/";

  /** The classifier id. */
  private static String classifierId = null;
  private String preCreatedClassifierId;

  /** The service. */
  private NaturalLanguageClassifier service;

  /*
   * (non-Javadoc)
   *
   * @see com.ibm.watson.developer_cloud.WatsonServiceTest#setUp()
   */
  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    String username = getProperty("natural_language_classifier.username");
    String password = getProperty("natural_language_classifier.password");

    Assume.assumeFalse("config.properties doesn't have valid credentials.",
        (username == null) || username.equals(PLACEHOLDER));

    service = new NaturalLanguageClassifier();
    service.setDefaultHeaders(getDefaultHeaders());
    service.setUsernameAndPassword(username, password);
    service.setEndPoint(getProperty("natural_language_classifier.url"));

    preCreatedClassifierId = getProperty("natural_language_classifier.classifier_id");
  }

  /**
   * Creates the classifier.
   *
   * @throws Exception the exception
   */
  @Test
  public void aCreate() throws Exception {
    final String classifierName = "devexp-available";

    InputStream trainingData = new FileInputStream(new File(RESOURCE + "weather_data_train.csv"));
    InputStream metadata = new ByteArrayInputStream(("{\"language\":\"en\",\"name\":\"" + classifierName + "\"}").getBytes());
    CreateClassifierOptions createOptions = new CreateClassifierOptions.Builder()
        .trainingData(trainingData)
        .metadata(metadata)
        .build();
    Classifier classifier = service.createClassifier(createOptions).execute();

    try {
      assertNotNull(classifier);
      assertEquals(Status.TRAINING, classifier.getStatus());
      assertEquals(classifierName, classifier.getName());
    } finally {
      classifierId = classifier.getClassifierId();
    }

  }

  /**
   * Test get classifier.
   */
  @Test
  public void bGetClassifier() {
    final Classifier classifier;

    try {
      GetClassifierOptions options = new GetClassifierOptions.Builder(classifierId).build();
      classifier = service.getClassifier(options).execute();
    } catch (NotFoundException e) {
      // #324: Classifiers may be empty, because of other tests interfering.
      // The build should not fail here, because this is out of our control.
      throw new AssumptionViolatedException(e.getMessage(), e);
    }
    assertNotNull(classifier);
    assertEquals(classifierId, classifier.getClassifierId());
    assertEquals(Classifier.Status.TRAINING, classifier.getStatus());
  }

  /**
   * Test get classifiers.
   */
  @Test
  public void cGetClassifiers() {

    final ClassifierList classifiers = service.listClassifiers().execute();
    assertNotNull(classifiers);

    // #324: Classifiers may be empty, because of other tests interfering.
    // The build should not fail here, because this is out of our control.
    Assume.assumeFalse(classifiers.getClassifiers().isEmpty());
  }

  /**
   * Test classify. Use the pre created classifier to avoid waiting for availability
   */
  @Test
  public void dClassify() {
    Classification classification = null;

    try {
      ClassifyOptions options = new ClassifyOptions.Builder()
          .classifierId(preCreatedClassifierId)
          .text("is it hot outside?")
          .build();
      classification = service.classify(options).execute();
    } catch (NotFoundException e) {
      // #324: Classifiers may be empty, because of other tests interfering.
      // The build should not fail here, because this is out of our control.
      throw new AssumptionViolatedException(e.getMessage(), e);
    }

    assertNotNull(classification);
    assertEquals("temperature", classification.getTopClass());
  }

  /**
   * Test delete classifier. Do not delete the pre created classifier. We need it for classify
   */
  @Test
  public void eDelete() {
    List<Classifier> classifiers = service.listClassifiers().execute().getClassifiers();

    for (Classifier classifier : classifiers) {
      if (!classifier.getClassifierId().equals(preCreatedClassifierId)) {
        DeleteClassifierOptions options = new DeleteClassifierOptions.Builder(classifier.getClassifierId()).build();
        service.deleteClassifier(options).execute();
      }
    }
  }

}
