import api from './axiosConfig';

export const getAccount = async (accountNumber) => {
  const response = await api.get(`/api/v1/account/${accountNumber}`);
  return response.data;
};

export const createAccount = async (type) => {
  const response = await api.post(`/api/v1/account/${type}`);
  return response.data;
};

export const deleteAccount = async (accountNumber) => {
  await api.delete(`/api/v1/account/${accountNumber}`);
};