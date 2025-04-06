package hello.advanced.app.v0;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV0 {

	public void orderItem(String itemId) {
		if (itemId.equals("ex")) {
			throw new IllegalStateException("예외 발생");
		}

		sleep(1000);
	}

	private void sleep(int mills) {
		try {
			Thread.sleep(mills);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}
}
