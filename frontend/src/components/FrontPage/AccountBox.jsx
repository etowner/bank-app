import React, { useState, useContext } from "react";
import { UserContext } from "../../UserContext";
import { Alert, Button, Form, Tab, Tabs, Card, Col, Row } from "react-bootstrap";

const AccountBox = () => {
  const { login, register, error } = useContext(UserContext);
  const [userID, setUserID] = useState("");
  const [password, setPassword] = useState("");

  const handleCreate = (event) => {
    event.preventDefault();
    register(userID, password);
  };

  const handleLog = (event) => {
    event.preventDefault();
    login(userID, password);
  };

  return (
    <Card style={{ width: "35rem" }} bg="secondary" text="light">
      <Card.Body>
        <Tabs defaultActiveKey="create" className="mb-3" fill>
          <Tab eventKey="create" title="Create Account">
            <Form>
              <Form.Group as={Row} className="mb-3 justify-content-md-center">
                <Form.Label column sm={6}>Enter userID:</Form.Label>
                <Col>
                  <Form.Control value={userID} onChange={(e) => setUserID(e.target.value)} />
                </Col>
              </Form.Group>

              <Form.Group as={Row} className="mb-3 justify-content-md-center">
                <Form.Label column>Enter password:</Form.Label>
                <Col sm={6}>
                  <Form.Control type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                </Col>
              </Form.Group>
              <Button variant="light" onClick={handleCreate} size="lg" className="mb-3">
                Submit
              </Button>
              {error && <Alert className="justify-center" variant="danger">{error}</Alert>}
            </Form>
          </Tab>
          <Tab eventKey="log" title="Log In">
            <Form>
              <Form.Group as={Row} className="mb-3 justify-content-md-center">
                <Form.Label column sm={6}>Enter userID:</Form.Label>
                <Col>
                  <Form.Control value={userID} onChange={(e) => setUserID(e.target.value)} />
                </Col>
              </Form.Group>

              <Form.Group as={Row} className="mb-3 justify-content-md-center">
                <Form.Label column>Enter password:</Form.Label>
                <Col sm={6}>
                  <Form.Control type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                </Col>
              </Form.Group>
              <Button variant="light" size="lg" onClick={handleLog} className="mb-3">
                Submit
              </Button>
              {error && <Alert className="justify-center" variant="danger">{error}</Alert>}
            </Form>
          </Tab>
        </Tabs>
      </Card.Body>
    </Card>
  );
};

export default AccountBox;
