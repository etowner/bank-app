import React from "react";
import { Button, Card, Col, Row } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

export default function AccList({ userID, accList }) {
  const navigate = useNavigate();

  function handleAccount(accountID) {
    navigate(`/${userID}/${accountID}`);
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
              <h3 key={account.accountID}>
                <Row className="mb-3">
                  <Col sm={6}>
                    {account.type} - {account.accountID}
                  </Col>
                  <Col sm={5} className="justify-content-end">
                    ${account.balance}
                    <Button
                      variant="link"
                      onClick={() => handleAccount(account.accountID)}
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
