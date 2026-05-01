import React, { useState, useContext } from "react";
import { UserContext } from "../../UserContext";
import { Alert, Button, Form, Tab, Tabs, Card, Col, Row } from "react-bootstrap";

const AuthForm = ({ onSubmit, buttonText }) => (
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
        <Button variant="light" onClick={onSubmit} size="lg" className="mb-3">
            {buttonText}
        </Button>
        {error && <Alert variant="danger">{error}</Alert>}
    </Form>
);

const AccountBox = () => {
  const { login, register, error, setError } = useContext(UserContext);
  const [userID, setUserID] = useState("");
  const [password, setPassword] = useState("");
  const [activeTab, setActiveTab] = useState("create");

  const handleCreate = (event) => {
    event.preventDefault();
    register(userID, password);
  };

  const handleLog = (event) => {
    event.preventDefault();
    login(userID, password);
  };

  const handleTabSwitch = (tab) => {
      setActiveTab(tab);
      setUserID("");
      setPassword("");
      setError(null);
  };

  return (
    <Card style={{ width: "35rem" }} bg="secondary" text="light">
      <Card.Body>
        <Tabs activeKey={activeTab} onSelect={handleTabSwitch} className="mb-3" fill>
          <Tab eventKey="create" title="Create Account">
            <AuthForm onSubmit={handleCreate} buttonText="Submit" />
          </Tab>
          <Tab eventKey="log" title="Log In">
            <AuthForm onSubmit={handleLog} buttonText="Submit" />
          </Tab>
        </Tabs>
      </Card.Body>
    </Card>
  );
};

export default AccountBox;
