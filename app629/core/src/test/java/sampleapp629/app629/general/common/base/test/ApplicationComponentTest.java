package sampleapp629.app629.general.common.base.test;

import io.oasp.module.test.common.base.ComponentTest;

import sampleapp629.app629.SpringBootApp;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * Abstract base class for {@link ComponentTest}s of this application.
 */
@SpringBootTest(classes = { SpringBootApp.class }, webEnvironment = WebEnvironment.NONE)
public abstract class ApplicationComponentTest extends ComponentTest {

  @Override
  protected void doTearDown() {
    super.doTearDown();
    TestUtil.logout();
  }

}
