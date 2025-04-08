package hello.advanced.app.v3;

import org.springframework.stereotype.Repository;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV3 {

	private final LogTrace trace;

	public void orderItem(String itemId) {
		TraceStatus status = null;

		try {
			status = trace.begin("OrderRepository.orderItem()");

			if (itemId.equals("ex")) {
				throw new IllegalStateException("예외 발생!");
			}

			sleep(1000);
			trace.end(status);
		} catch (Exception e) {
			trace.exception(status, e);
			throw e;
		}
	}

	private void sleep(int mills) {
		try {
			Thread.sleep(mills);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}
}
