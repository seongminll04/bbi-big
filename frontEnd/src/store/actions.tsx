export const setLogin = (value: 
    {
        nickname: string|null;
        profile: string|null;
    } | null) => ({
    type: "SET_LOGIN",
    payload: value,
});