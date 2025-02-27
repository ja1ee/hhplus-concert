import { callApi } from './callApi.js';

export const reserveSeat = async (userId) => {
    const headers = {
        "userId": userId
    };
    const path = "/reservation";

    const randomId = Math.floor(Math.random() * 20) + 1;

    const body = {
        userId : userId,
        seatDto: {"id": randomId,
                "scheduleId": 1,
                "seatNo": randomId,
                "price": "1000",
                "isReserved": false},
        concertDate: "2025-01-01",
        concertId: 1,
        finalPrice: "1000"
    };

    return await callApi({method: "POST", headers, path, body })
}