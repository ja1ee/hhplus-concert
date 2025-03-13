import http from "k6/http";

export const signToken = async (userId) => {
    const url = `http://localhost:8080/tokens/${userId}`;
    const res = http.get(url);

    return JSON.parse(res.body);
}