package server.book_library.global.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "회원의 ID를 찾을 수 없습니다."),
    BOOK_NOT_FOUND(404, "책을 찾을 수 없습니다."),
    LIBRARY_NOT_FOUND(404, "도서관을 찾을 수 없습니다."),
    LIBRARY_INVENTORY_OUT_OF_STOCK(404,"도서 재고가 없습니다."),
    LOAN_NOT_FOUND(404,"대여 ID를 찾을 수 없습니다"),
    LOAN_NOT_ALLOW(403,"도서관에 해당 책이 모두 대여중입니다."),
    MEMBER_HAS_PENALTY(403,"회원님은 도서 연체로 인해 대여가 불가능 합니다."),
    EXCEEDS_MAXIMUM_QUANTITY(403,"책을 빌릴 수 없습니다. 대여는 최대 5권까지 가능합니다."),
    MEMBER_NOT_MATCH(403,"해당 책을 대여하지 않은 회원 입니다."),
    ALREADY_RETURNED_BOOKS(403,"이미 반납된 책 입니다.");

    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
