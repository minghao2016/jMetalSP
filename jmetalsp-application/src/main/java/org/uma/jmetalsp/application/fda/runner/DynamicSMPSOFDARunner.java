package  org.uma.jmetalsp.application.fda.runner;

import org.uma.jmetalsp.algorithm.DynamicSMPSO;
import org.uma.jmetalsp.application.JMetalSPApplication;
import org.uma.jmetalsp.application.fda.algorithm.DynamicSMPSOBuilder;
import org.uma.jmetalsp.application.fda.problem.FDA1;
import org.uma.jmetalsp.application.fda.problem.FDA1ProblemBuilder;
import org.uma.jmetalsp.application.fda.problem.FDAUpdateData;
import org.uma.jmetalsp.application.fda.sparkutil.StreamingConfigurationFDA;
import org.uma.jmetalsp.application.fda.streamingDataSource.StreamingKafkaFDA;
import org.uma.jmetalsp.consumer.impl.LocalDirectoryOutputConsumer;
import org.uma.jmetalsp.consumer.impl.SimpleSolutionListConsumer;
import org.uma.jmetalsp.util.spark.SparkRuntime;

import java.io.IOException;

/**
 * @author Cristóbal Barba <cbarba@lcc.uma.es>
 */
public class DynamicSMPSOFDARunner {
  public static void main(String[] args) throws IOException, InterruptedException {
    JMetalSPApplication<
            FDAUpdateData,
            FDA1,
            DynamicSMPSO> application = new JMetalSPApplication<>();
    StreamingConfigurationFDA streamingConfigurationFDA= new StreamingConfigurationFDA();


    String kafkaServer="localhost";
    int kafkaPort=2181;
    String kafkaTopic="fdadata";
    streamingConfigurationFDA.initializeKafka(kafkaServer,kafkaPort,kafkaTopic);
    application
            .setSparkRuntime(new SparkRuntime(2))
            .setProblemBuilder(new FDA1ProblemBuilder(100,2))
            .setAlgorithmBuilder(new DynamicSMPSOBuilder())
            .addAlgorithmDataConsumer(new SimpleSolutionListConsumer())
            .addAlgorithmDataConsumer(new LocalDirectoryOutputConsumer("/Users/cristobal/Documents/tesis/fda"))
            .addStreamingDataSource(new StreamingKafkaFDA(streamingConfigurationFDA))
            .run();
  }
}
