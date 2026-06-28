import { useState } from "react";
import { useParams } from "react-router-dom";
import { Button, Form, Row, Col, Alert } from "react-bootstrap";
import { withdraw } from "../../api/transactionApi";
import type { Account } from "../../lib/types";

interface WithdrawProps {
  balance: number | undefined;
  setAccount: (account: Account) => void;
  fetchAccountData: () => Promise<void>;
}

export default function Withdraw({ balance, setAccount, fetchAccountData }: WithdrawProps) {
  const [amount, setAmount] = useState('');
  const { accountNumber } = useParams<{ accountNumber: string }>();
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);


  const handleWithdrawClick = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
    const parsedAmount = parseFloat(amount); 

    if (isNaN(parsedAmount) || parsedAmount <= 0) {
      setError("Invalid withdrawal amount. Please enter a valid amount.");
      return;
    }
    if (balance === undefined || balance - parsedAmount < 0) {
      setError("Insufficient funds.");
      return;
    }

    setLoading(true);

    try {
      const updateAccount = await withdraw(accountNumber!, parsedAmount);
      setError(null);
      setAmount('');
      setAccount(updateAccount);
      void await fetchAccountData();
    } catch (error) {
      setError("Withdrawal failed. Please try again.");
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <Form.Group as={Row} className="mb-3 ">
        <Form.Label column sm={5}>
          Enter an amount:
        </Form.Label>
        <Col sm={3}>
          <Form.Control
            type="number"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            placeholder="Enter amount"
            min="0"
            step="0.01"
          />
        </Col>
        <Col sm={2}>
          <Button onClick={(e) => void handleWithdrawClick(e)} className="mb-3">
            {loading ? <>Loading..</> : <>Submit</>}
          </Button>
        </Col>
      </Form.Group>
      <Row className="justify-content-md-center">
        <Col sm={6}>{error && <Alert variant="danger">{error}</Alert>}</Col>
      </Row>
    </div>
  );
}
