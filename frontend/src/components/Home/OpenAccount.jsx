import React from "react";
import { Accordion, Button, Card, ListGroup } from "react-bootstrap";
import { useAccordionButton } from "react-bootstrap/AccordionButton";

function CustomToggle({ children, eventKey, onToggle }) {
  const showAcc = useAccordionButton(eventKey, onToggle);
  
  return (
    <Button variant="dark" onClick={showAcc} className="mb-3">
      {children}
    </Button>
  );
}

export default function OpenAccount({ openAcc, setError}) {
  
  return (
    <div>
      <Accordion defaultActiveKey="1">
        <CustomToggle eventKey="0" onToggle={() => setError(null)}>Open a new account</CustomToggle>
        <Accordion.Collapse eventKey="0">
          <Card.Body>
            <ListGroup>
              <ListGroup.Item action onClick={() => openAcc("Checkings")}>
                Checkings
              </ListGroup.Item>
              <ListGroup.Item action onClick={() => openAcc("Savings")}>
                Savings
              </ListGroup.Item>
            </ListGroup>
          </Card.Body>
        </Accordion.Collapse>
      </Accordion>
    </div>
  );
}
