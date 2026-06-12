import { Button, Card, Col, Row } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { Account } from "../../types";

export default function AccList({ accList }: { accList: Account[] }) {
  const navigate = useNavigate();

  function handleAccount(accountNumber: string) {
    navigate(`/account/${accountNumber}`);
  }

  return (
    <div>
      <Card.Header style={{ color: "white", backgroundColor: "#282c34" }}>
        <h2>Accounts</h2>
      </Card.Header>
      <Card.Body>
        {accList &&
          accList.map((account) => {
            return (
              <h3 key={account.accountNumber}>
                <Row className="mb-3">
                  <Col sm={6}>
                    {account.type} - {account.accountNumber}
                  </Col>
                  <Col sm={5} className="justify-content-end">
                    {account.balance}
                    <Button
                      variant="link"
                      onClick={() => handleAccount(account.accountNumber)}
                    >
                      View
                    </Button>
                  </Col>
                </Row>
              </h3>
            );
          })}
      </Card.Body>
    </div>
  );
}
