/*
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
package com.ibm.watson.developer_cloud.natural_language_classifier.v1.model;

import java.io.InputStream;

import com.ibm.watson.developer_cloud.service.model.GenericModel;
import com.ibm.watson.developer_cloud.util.Validator;

/**
 * The createClassifier options.
 */
public class CreateClassifierOptions extends GenericModel {

  private InputStream metadata;
  private InputStream trainingData;

  /**
   * Builder.
   */
  public static class Builder {
    private InputStream metadata;
    private InputStream trainingData;

    private Builder(CreateClassifierOptions createClassifierOptions) {
      metadata = createClassifierOptions.metadata;
      trainingData = createClassifierOptions.trainingData;
    }

    /**
     * Instantiates a new builder.
     */
    public Builder() {
    }

    /**
     * Instantiates a new builder with required properties.
     *
     * @param metadata the metadata
     * @param trainingData the trainingData
     */
    public Builder(InputStream metadata, InputStream trainingData) {
      this.metadata = metadata;
      this.trainingData = trainingData;
    }

    /**
     * Builds a CreateClassifierOptions.
     *
     * @return the createClassifierOptions
     */
    public CreateClassifierOptions build() {
      return new CreateClassifierOptions(this);
    }

    /**
     * Set the metadata.
     *
     * @param metadata the metadata
     * @return the CreateClassifierOptions builder
     */
    public Builder metadata(InputStream metadata) {
      this.metadata = metadata;
      return this;
    }

    /**
     * Set the trainingData.
     *
     * @param trainingData the trainingData
     * @return the CreateClassifierOptions builder
     */
    public Builder trainingData(InputStream trainingData) {
      this.trainingData = trainingData;
      return this;
    }
  }

  private CreateClassifierOptions(Builder builder) {
    Validator.notNull(builder.metadata, "metadata cannot be null");
    Validator.notNull(builder.trainingData, "trainingData cannot be null");
    metadata = builder.metadata;
    trainingData = builder.trainingData;
  }

  /**
   * New builder.
   *
   * @return a CreateClassifierOptions builder
   */
  public Builder newBuilder() {
    return new Builder(this);
  }

  /**
   * Gets the metadata.
   *
   * Metadata in JSON format. The metadata identifies the language of the data, and an optional name to identify the
   * classifier. For details, see the [API
   * reference](https://www.ibm.com/watson/developercloud/natural-language-classifier/api/v1/#create_classifier).
   *
   * @return the metadata
   */
  public InputStream metadata() {
    return metadata;
  }

  /**
   * Gets the trainingData.
   *
   * Training data in CSV format. Each text value must have at least one class. The data can include up to 15,000
   * records. For details, see [Using your own
   * data](https://www.ibm.com/watson/developercloud/doc/natural-language-classifier/using-your-data.html).
   *
   * @return the trainingData
   */
  public InputStream trainingData() {
    return trainingData;
  }
}
