package o.w.o.domain.example.enumentity;

import o.w.o.domain.ResourceTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EnumEntityExampleRepositoryTest extends ResourceTest {
  @Autowired
  private EnumEntityExampleRepository enumEntityExampleRepository;

  @Test
  public void findPreset() {
    var entity = enumEntityExampleRepository.findById(EnumEntityExample.Enum.X.getId());
    Assertions.assertTrue(entity.isPresent());
    Assertions.assertEquals(entity.get().getId(), EnumEntityExample.Enum.X.getId());
  }

  @BeforeEach
  public void createNonPreset() {
    enumEntityExampleRepository.save(new EnumEntityExample().setId(10).setName("Ten"));

    if (!enumEntityExampleRepository.existsById(11)) {
      enumEntityExampleRepository.save(new EnumEntityExample().setId(11).setName("Eleven"));
    }
  }

  @AfterEach
  public void deleteNonPreset() {
    enumEntityExampleRepository.deleteById(10);
  }

  @Test
  public void findNonPreset() {
    var entity = enumEntityExampleRepository.findById(10);
    Assertions.assertTrue(entity.isPresent());
  }
}