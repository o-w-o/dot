package ink.o.w.o.resource.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import ink.o.w.o.resource.sample.domain.Dot;
import ink.o.w.o.resource.sample.domain.DotPayload;
import ink.o.w.o.resource.sample.domain.DotPointI;
import ink.o.w.o.resource.sample.domain.DotPointX;
import ink.o.w.o.resource.sample.repository.DotPayloadRepository;
import ink.o.w.o.resource.sample.repository.DotPointIRepository;
import ink.o.w.o.resource.sample.repository.DotPointXRepository;
import ink.o.w.o.resource.sample.repository.DotRepository;
import ink.o.w.o.util.JSONHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@SpringBootTest
@AutoConfigureDataJpa
public class SampleTest {
  @Autowired
  private DotRepository dotRepository;

  @Autowired
  private DotPayloadRepository dotPayloadRepository;

  @Autowired
  private DotPointIRepository dotPointIRepository;

  @Autowired
  private DotPointXRepository dotPointXRepository;

  @Autowired
  private JSONHelper jsonHelper;

  public void printDot() throws JsonProcessingException {
    logger.info("dot[12] -> {}", jsonHelper.toJSONString(dotRepository.findById(12L).get()));
  }

  public void printDot(Dot dot) throws JsonProcessingException {
    logger.info("dot[12] -> {}", jsonHelper.toJSONString(dot));
  }

  @Test
  public void mockData() throws JsonProcessingException {
    if(dotRepository.existsById(12L)){
      dotRepository.deleteById(12L);
    }

    DotPointI dotPointI = new DotPointI().setName("dotPointI");
    dotPointIRepository.save(dotPointI);

    Set<DotPointI> dotPointISet = new HashSet<>();
    dotPointISet.add(dotPointI);

    DotPayload dotPayload = new DotPayload().setName("dotPayload");

    Dot dot = new Dot()
        .setName("dot")
        .setId(12L)
        .setIPoints(dotPointISet)
        .setPayload(dotPayload);

    Dot dot1 = dotRepository.save(dot);
    printDot(dot1);

    logger.info("dot id -> {}", dot1.getId());

    DotPointX dotPointX = new DotPointX().setId(12L).setName("dotPointX");
    dotPointX.setDot(dot1);
    Set<DotPointX> dotPointXSet = new HashSet<>(); dotPointXSet.add(dotPointX);
    dot.setXPonints(dotPointXSet);

    dotPointXRepository.save(dotPointX);

    dotPointXRepository.delete(dotPointX);

    printDot();
    dot1.getIPoints();
    printDot(dot1);

    // dotPointIRepository.deleteAll(dot1.getIPoints());
    // printDot();

    // dotPayloadRepository.delete(dot1.getPayload());
    // printDot();
  }
}
