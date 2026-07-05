interface User {
    username: string;
    accounts: Account[];
    numOfAccounts: number;
}

interface Account {
    accountNumber: string;
    type: string;
    balance: number;
}

interface Transaction {
    id: string;
    type: string;
    amount: number;
    counterparty: string;
    timestamp: string;
}

export type { User, Account, Transaction };