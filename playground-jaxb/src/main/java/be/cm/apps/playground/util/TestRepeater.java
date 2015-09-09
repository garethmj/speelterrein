package be.cm.apps.playground.util;

import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class TestRepeater extends TestWatchman {

	@Override
		public Statement apply(final Statement base, final FrameworkMethod method,
				Object target) {
			return new Statement() {
				@Override
				public void evaluate() throws Throwable {
					starting(method);
					try {
						for (int i=0;i<10;++i) {
							base.evaluate();
						}
						succeeded(method);
					} catch (Throwable t) {
						failed(t, method);
						throw t;
					} finally {
						finished(method);
					}
				}
			};
	}

}
