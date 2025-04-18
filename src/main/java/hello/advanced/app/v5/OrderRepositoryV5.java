package hello.advanced.app.v5;

import org.springframework.stereotype.Repository;

import hello.advanced.trace.callback.TraceTemplate;
import hello.advanced.trace.logtrace.LogTrace;

@Repository
public class OrderRepositoryV5 {

	private final TraceTemplate template;

	public OrderRepositoryV5(LogTrace trace) {
		this.template = new TraceTemplate(trace);
	}

	public void orderItem(String itemId) {
		template.execute("OrderRepository.orderItem()", () -> {
			if (itemId.equals("ex")) {
				throw new IllegalStateException("예외 발생!");
			}

			sleep(1000);
			return null;
		});
	}

	private void sleep(int mills) {
		try {
			Thread.sleep(mills);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}
}
