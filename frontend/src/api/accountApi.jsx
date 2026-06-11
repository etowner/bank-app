import api from './axiosConfig';

export const getAccount = async (accountNumber) => {
  const response = await api.get(`/api/v1/account/${accountNumber}`);
  // console.log("getAccount response:", response);
  return response.data;
};

export const createAccount = async (type) => {
  const response = await api.post(`/api/v1/account/open/${type}`);
  console.log("createAccount response:", response);
  return response.data;
};

export const deleteAccount = async (accountNumber) => {
  await api.delete(`/api/v1/account/${accountNumber}`);
};