import { useState } from "react";
import { Button, Card, Col, Form, Row, Alert } from "react-bootstrap";
import { useUserContext } from "../../UserContext";
import { transfer } from "../../api/transactionApi";

export default function Transfer() {
  const { fetchUser } = useUserContext();
  const [amount, setAmount] = useState(0);
  const [accountNumber1, setAccountNumber1] = useState("");
  const [accountNumber2, setAccountNumber2] = useState("");
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const handleTransferClick = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
    
    if (isNaN(amount) || amount <= 0) {
      setError("Invalid transfer amount. Please enter a valid amount.");
      return;
    }
    if (accountNumber1 === accountNumber2) {
      setError("Source and destination accounts must be different.");
      return;
    }
    if (!accountNumber1 || !accountNumber2) {
      setError("Please enter both account IDs.");
      return;
    }
    setLoading(true);
    try {
        await transfer(accountNumber1, accountNumber2, amount);
        setAmount(0);
        setAccountNumber1("");
        setAccountNumber2("");
        setError(null);
        await fetchUser();
    } catch (err: any) {
        setError(err.response?.data || "Transfer failed. Please try again.");
        console.error("Transfer error:", err.response ? err.response : err.request ? err.request : err.message);
    } finally {
        setLoading(false);
    }
    fetchUser();
};

  return (
    <div>
      <Card border="dark">
        <Card.Header style={{ color: "white", backgroundColor: "#282c34" }}>
          <h2>Transfer</h2>
        </Card.Header>
        <Card.Body>
          <Form.Group as={Row} className="mb-1">
            <Form.Label column sm={5}>
              <h3>Transfer from:</h3>
            </Form.Label>
            <Col sm={1}></Col>
            <Col sm={4}>
              <Form.Control
                value={accountNumber1}
                onChange={(e) => setAccountNumber1(e.target.value)}
              />
            </Col>
          </Form.Group>
          <Form.Group as={Row} className="mb-1">
            <Form.Label column sm={5}>
              <h3> Transfer to: </h3>
            </Form.Label>
            <Col sm={1}></Col>
            <Col sm={4}>
              <Form.Control
                value={accountNumber2}
                onChange={(e) => setAccountNumber2(e.target.value)}
              />
            </Col>
          </Form.Group>
          <Form.Group as={Row} className="mb-1">
            <Form.Label column sm={6}>
              <h3>Transfer amount:</h3>
            </Form.Label>
            <Col sm={4}>
              <Form.Control
                value={amount}
                onChange={(e) => setAmount(parseFloat(e.target.value))}
              />
            </Col>
          </Form.Group>
          <Row className="justify-content-md-center">
            <Col sm={2}>
              <Button
                variant="dark"
                onClick={handleTransferClick}
                className="mb-3"
              >
                {loading ? <>Loading..</> : <>Submit</>}
              </Button>
            </Col>
          </Row>
          {
            <Row className="justify-content-md-center">
              <Col sm={7}>
                {error && <Alert variant="danger">{error}</Alert>}
              </Col>
            </Row>
          }
        </Card.Body>
      </Card>
    </div>
  );
}
