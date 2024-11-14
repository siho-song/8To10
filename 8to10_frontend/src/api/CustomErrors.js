class CustomErrors extends Error {
    constructor(status, code, message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    toString() {
        return `HTTP 상태 코드 : ${this.status}\n에러 코드 : ${this.code}\n에러 메시지 : ${this.message}`;
    }
}

export default CustomErrors;