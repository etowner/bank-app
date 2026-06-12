import { Accordion, Button, Card, ListGroup, useAccordionButton} from "react-bootstrap";

function CustomToggle({ children, eventKey, onToggle }: { children: React.ReactNode; eventKey: string; onToggle: () => void }) {
  const showAcc = useAccordionButton(eventKey, onToggle);
  
  return (
    <Button variant="dark" onClick={showAcc} className="mb-3">
      {children}
    </Button>
  );
}

export default function OpenAccount({ openAcc, setError }: { openAcc: (accountType: string) => void; setError: (error: string | null) => void }) {
  
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
