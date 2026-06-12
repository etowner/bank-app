import { useState } from "react";
import { useParams } from "react-router-dom";
import { Button, Form, Row, Col, Alert } from "react-bootstrap";
import { withdraw } from "../../api/transactionApi";
import { Account } from "../../types";


export default function Withdraw({ balance, setAccount, fetchAccountData }: 
  { balance: number; setAccount: (account: any) => void; fetchAccountData: () => Promise<void> }) {
  const [amount, setAmount] = useState(0);
  const { accountNumber } = useParams();
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleWithdrawClick = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (isNaN(amount) || amount <= 0) {
      setError("Invalid withdrawal amount. Please enter a valid amount.");
      return;
    }
    if (balance - amount < 0) {
      setError("Insufficient funds.");
      return;
    }
    setLoading(true);
    try {
      const updateAccount = await withdraw(accountNumber, parseFloat(amount));
      setError("");
      setAmount(0);
      setAccount(updateAccount);
    } catch (error) {
      setError("Withdrawal failed. Please try again.");
      console.error(error);
    } finally {
      setLoading(false);
    }

    await fetchAccountData();
  };

  return (
    <div>
      <Form.Group as={Row} className="mb-3 ">
        <Form.Label column sm={5}>
          Enter an amount:
        </Form.Label>
        <Col sm={3}>
          <Form.Control
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
          />
        </Col>
        <Col sm={2}>
          <Button onClick={handleWithdrawClick} className="mb-3">
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
