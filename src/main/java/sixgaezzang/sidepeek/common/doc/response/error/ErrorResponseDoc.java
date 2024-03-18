package sixgaezzang.sidepeek.common.doc.response.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ErrorResponseDoc {
    public static final String BAD_REQUEST_RESPONSE1 = """
        {
          "status": "BAD_REQUEST",
          "code": 400,
          "message": "Error Message"
        }
        """;
    public static final String BAD_REQUEST_RESPONSE2 = """
        [
          {
            "status": "BAD_REQUEST",
            "code": 400,
            "message": "Field Error Message"
          },
          {
            "status": "BAD_REQUEST",
            "code": 400,
            "message": "Field Error Message"
          }
        ]
        """;
    public static final String UNAUTHORIZED_RESPONSE = """
        {
          "status": "UNAUTHORIZED",
          "code": 401,
          "message": "Error Message"
        }
        """;
    public static final String FORBIDDEN_RESPONSE = """
        {
          "status": "FORBIDDEN",
          "code": 403,
          "message": "Error Message"
        }
        """;
    public static final String NOT_FOUND_RESPONSE = """
        {
          "status": "NOT_FOUND",
          "code": 404,
          "message": "Error Message"
        }
        """;
    public static final String CONFLICT_RESPONSE = """
        {
          "status": "CONFLICT",
          "code": 409,
          "message": "Error Message"
        }
        """;
    public static final String INTERNAL_SERVER_ERROR_RESPONSE = """
        {
          "status": "INTERNAL_SERVER_ERROR",
          "code": 500,
          "message": "Error Message"
        }
        """;
}
