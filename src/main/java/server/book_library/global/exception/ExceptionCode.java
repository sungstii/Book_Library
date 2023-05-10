package server.book_library.global.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "회원의 ID를 찾을 수 없습니다."),
    BOOK_NOT_FOUND(404, "책을 찾을 수 없습니다."),
    LIBRARY_NOT_FOUND(404, "도서관을 찾을 수 없습니다."),
    LIBRARY_INVENTORY_OUT_OF_STOCK(404,"도서 재고가 없습니다."),
    LOAN_NOT_FOUND(404, "대여를 할 수 없습니다");

    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
