import { callApi } from './callApi.js';

export const payForSeat = async (data) => {
    const headers = {
        "userId": data.userId
    };
    const path = "/reservation/payment";

    const body = {
        id: data.id,
    };

    return await callApi({method: "POST", headers, path, body })
}