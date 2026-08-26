import { Accordion, Button, Card, ListGroup, useAccordionButton, AccordionContext} from "react-bootstrap";
import { use } from "react";

function CustomToggle({ children, eventKey, onToggle }: { children: React.ReactNode; eventKey: string; onToggle: () => void }) {
 
  const { activeEventKey } = use(AccordionContext);
   const showAcc = useAccordionButton(eventKey, onToggle);
    const isExpanded = activeEventKey === eventKey;
  
  return (
    <Button variant="dark" onClick={showAcc} className="mb-3" aria-expanded={isExpanded}>
      {children}
    </Button>
  );
}

interface OpenAccountProps {
  openAcc: (accountType: string) => void;
  setError: (error: string | null) => void;
}

export default function OpenAccount({ openAcc, setError }: OpenAccountProps) {
  
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
